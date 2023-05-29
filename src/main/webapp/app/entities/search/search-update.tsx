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
import { ISearchParameter } from 'app/shared/model/search-parameter.model';
import { getEntities as getSearchParameters } from 'app/entities/search-parameter/search-parameter.reducer';
import { ISearch } from 'app/shared/model/search.model';
import { getEntity, updateEntity, createEntity, reset } from './search.reducer';

export const SearchUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const searchParameters = useAppSelector(state => state.searchParameter.entities);
  const searchEntity = useAppSelector(state => state.search.entity);
  const loading = useAppSelector(state => state.search.loading);
  const updating = useAppSelector(state => state.search.updating);
  const updateSuccess = useAppSelector(state => state.search.updateSuccess);

  const handleClose = () => {
    navigate('/search');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getSearchParameters({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.date = convertDateTimeToServer(values.date);

    const entity = {
      ...searchEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      searchParameter: searchParameters.find(it => it.id.toString() === values.searchParameter.toString()),
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
          date: displayDefaultDateTime(),
        }
      : {
          ...searchEntity,
          date: convertDateTimeFromServer(searchEntity.date),
          user: searchEntity?.user?.id,
          searchParameter: searchEntity?.searchParameter?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="betterSearchSpotifyApp.search.home.createOrEditLabel" data-cy="SearchCreateUpdateHeading">
            <Translate contentKey="betterSearchSpotifyApp.search.home.createOrEditLabel">Create or edit a Search</Translate>
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
                  id="search-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('betterSearchSpotifyApp.search.name')}
                id="search-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('betterSearchSpotifyApp.search.date')}
                id="search-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('betterSearchSpotifyApp.search.createPlayist')}
                id="search-createPlayist"
                name="createPlayist"
                data-cy="createPlayist"
                check
                type="checkbox"
              />
              <ValidatedField
                id="search-user"
                name="user"
                data-cy="user"
                label={translate('betterSearchSpotifyApp.search.user')}
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
              <ValidatedField
                id="search-searchParameter"
                name="searchParameter"
                data-cy="searchParameter"
                label={translate('betterSearchSpotifyApp.search.searchParameter')}
                type="select"
              >
                <option value="" key="0" />
                {searchParameters
                  ? searchParameters.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/search" replace color="info">
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

export default SearchUpdate;
