import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISearchParameter } from 'app/shared/model/search-parameter.model';
import { SearchParameterType } from 'app/shared/model/enumerations/search-parameter-type.model';
import { SearchParameterAttribute } from 'app/shared/model/enumerations/search-parameter-attribute.model';
import { AlbumType } from 'app/shared/model/enumerations/album-type.model';
import { getEntity, updateEntity, createEntity, reset } from './search-parameter.reducer';

export const SearchParameterUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const searchParameterEntity = useAppSelector(state => state.searchParameter.entity);
  const loading = useAppSelector(state => state.searchParameter.loading);
  const updating = useAppSelector(state => state.searchParameter.updating);
  const updateSuccess = useAppSelector(state => state.searchParameter.updateSuccess);
  const searchParameterTypeValues = Object.keys(SearchParameterType);
  const searchParameterAttributeValues = Object.keys(SearchParameterAttribute);
  const albumTypeValues = Object.keys(AlbumType);

  const handleClose = () => {
    navigate('/search-parameter');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...searchParameterEntity,
      ...values,
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
          type: 'STARTSWITH',
          attributeName: 'ALBUM',
          albumType: 'ALBUM',
          ...searchParameterEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="betterSearchSpotifyApp.searchParameter.home.createOrEditLabel" data-cy="SearchParameterCreateUpdateHeading">
            <Translate contentKey="betterSearchSpotifyApp.searchParameter.home.createOrEditLabel">
              Create or edit a SearchParameter
            </Translate>
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
                  id="search-parameter-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('betterSearchSpotifyApp.searchParameter.type')}
                id="search-parameter-type"
                name="type"
                data-cy="type"
                type="select"
              >
                {searchParameterTypeValues.map(searchParameterType => (
                  <option value={searchParameterType} key={searchParameterType}>
                    {translate('betterSearchSpotifyApp.SearchParameterType.' + searchParameterType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('betterSearchSpotifyApp.searchParameter.attributeName')}
                id="search-parameter-attributeName"
                name="attributeName"
                data-cy="attributeName"
                type="select"
              >
                {searchParameterAttributeValues.map(searchParameterAttribute => (
                  <option value={searchParameterAttribute} key={searchParameterAttribute}>
                    {translate('betterSearchSpotifyApp.SearchParameterAttribute.' + searchParameterAttribute)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('betterSearchSpotifyApp.searchParameter.albumType')}
                id="search-parameter-albumType"
                name="albumType"
                data-cy="albumType"
                type="select"
              >
                {albumTypeValues.map(albumType => (
                  <option value={albumType} key={albumType}>
                    {translate('betterSearchSpotifyApp.AlbumType.' + albumType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('betterSearchSpotifyApp.searchParameter.searchValue')}
                id="search-parameter-searchValue"
                name="searchValue"
                data-cy="searchValue"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/search-parameter" replace color="info">
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

export default SearchParameterUpdate;
