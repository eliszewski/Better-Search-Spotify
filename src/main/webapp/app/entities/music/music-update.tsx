import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISearch } from 'app/shared/model/search.model';
import { getEntities as getSearches } from 'app/entities/search/search.reducer';
import { IMusic } from 'app/shared/model/music.model';
import { getEntity, updateEntity, createEntity, reset } from './music.reducer';

export const MusicUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const searches = useAppSelector(state => state.search.entities);
  const musicEntity = useAppSelector(state => state.music.entity);
  const loading = useAppSelector(state => state.music.loading);
  const updating = useAppSelector(state => state.music.updating);
  const updateSuccess = useAppSelector(state => state.music.updateSuccess);

  const handleClose = () => {
    navigate('/music');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSearches({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...musicEntity,
      ...values,
      search: searches.find(it => it.id.toString() === values.search.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...musicEntity,
          search: musicEntity?.search?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="betterSearchSpotifyApp.music.home.createOrEditLabel" data-cy="MusicCreateUpdateHeading">
            <Translate contentKey="betterSearchSpotifyApp.music.home.createOrEditLabel">Create or edit a Music</Translate>
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
                  id="music-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('betterSearchSpotifyApp.music.artist')}
                id="music-artist"
                name="artist"
                data-cy="artist"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('betterSearchSpotifyApp.music.title')}
                id="music-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('betterSearchSpotifyApp.music.album')}
                id="music-album"
                name="album"
                data-cy="album"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('betterSearchSpotifyApp.music.uri')}
                id="music-uri"
                name="uri"
                data-cy="uri"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('betterSearchSpotifyApp.music.releasedYear')}
                id="music-releasedYear"
                name="releasedYear"
                data-cy="releasedYear"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('betterSearchSpotifyApp.music.externalUrl')}
                id="music-externalUrl"
                name="externalUrl"
                data-cy="externalUrl"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('betterSearchSpotifyApp.music.explicit')}
                id="music-explicit"
                name="explicit"
                data-cy="explicit"
                check
                type="checkbox"
              />
              <ValidatedField
                id="music-search"
                name="search"
                data-cy="search"
                label={translate('betterSearchSpotifyApp.music.search')}
                type="select"
              >
                <option value="" key="0" />
                {searches
                  ? searches.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/music" replace color="info">
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

export default MusicUpdate;
