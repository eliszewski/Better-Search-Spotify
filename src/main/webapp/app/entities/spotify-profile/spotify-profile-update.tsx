import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ISpotifyProfile } from 'app/shared/model/spotify-profile.model';
import { getEntity, updateEntity, createEntity, reset } from './spotify-profile.reducer';

export const SpotifyProfileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const spotifyProfileEntity = useAppSelector(state => state.spotifyProfile.entity);
  const loading = useAppSelector(state => state.spotifyProfile.loading);
  const updating = useAppSelector(state => state.spotifyProfile.updating);
  const updateSuccess = useAppSelector(state => state.spotifyProfile.updateSuccess);

  const handleClose = () => {
    navigate('/spotify-profile');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.accessTokenExpiration = convertDateTimeToServer(values.accessTokenExpiration);
    values.refreshTokenExpiration = convertDateTimeToServer(values.refreshTokenExpiration);

    const entity = {
      ...spotifyProfileEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          accessTokenExpiration: displayDefaultDateTime(),
          refreshTokenExpiration: displayDefaultDateTime(),
        }
      : {
          ...spotifyProfileEntity,
          accessTokenExpiration: convertDateTimeFromServer(spotifyProfileEntity.accessTokenExpiration),
          refreshTokenExpiration: convertDateTimeFromServer(spotifyProfileEntity.refreshTokenExpiration),
          user: spotifyProfileEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="betterSearchSpotifyApp.spotifyProfile.home.createOrEditLabel" data-cy="SpotifyProfileCreateUpdateHeading">
            <Translate contentKey="betterSearchSpotifyApp.spotifyProfile.home.createOrEditLabel">Create or edit a SpotifyProfile</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="spotify-profile-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('betterSearchSpotifyApp.spotifyProfile.accessToken')}
                id="spotify-profile-accessToken"
                name="accessToken"
                data-cy="accessToken"
                type="text"
              />
              <ValidatedField
                label={translate('betterSearchSpotifyApp.spotifyProfile.refreshToken')}
                id="spotify-profile-refreshToken"
                name="refreshToken"
                data-cy="refreshToken"
                type="text"
              />
              <ValidatedField
                label={translate('betterSearchSpotifyApp.spotifyProfile.accessTokenExpiration')}
                id="spotify-profile-accessTokenExpiration"
                name="accessTokenExpiration"
                data-cy="accessTokenExpiration"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('betterSearchSpotifyApp.spotifyProfile.refreshTokenExpiration')}
                id="spotify-profile-refreshTokenExpiration"
                name="refreshTokenExpiration"
                data-cy="refreshTokenExpiration"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="spotify-profile-user"
                name="user"
                data-cy="user"
                label={translate('betterSearchSpotifyApp.spotifyProfile.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/spotify-profile" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default SpotifyProfileUpdate;
