import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './search-parameter.reducer';

export const SearchParameterDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const searchParameterEntity = useAppSelector(state => state.searchParameter.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="searchParameterDetailsHeading">
          <Translate contentKey="betterSearchSpotifyApp.searchParameter.detail.title">SearchParameter</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{searchParameterEntity.id}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="betterSearchSpotifyApp.searchParameter.type">Type</Translate>
            </span>
          </dt>
          <dd>{searchParameterEntity.type}</dd>
          <dt>
            <span id="attributeName">
              <Translate contentKey="betterSearchSpotifyApp.searchParameter.attributeName">Attribute Name</Translate>
            </span>
          </dt>
          <dd>{searchParameterEntity.attributeName}</dd>
          <dt>
            <span id="albumType">
              <Translate contentKey="betterSearchSpotifyApp.searchParameter.albumType">Album Type</Translate>
            </span>
          </dt>
          <dd>{searchParameterEntity.albumType}</dd>
          <dt>
            <span id="searchValue">
              <Translate contentKey="betterSearchSpotifyApp.searchParameter.searchValue">Search Value</Translate>
            </span>
          </dt>
          <dd>{searchParameterEntity.searchValue}</dd>
        </dl>
        <Button tag={Link} to="/search-parameter" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/search-parameter/${searchParameterEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SearchParameterDetail;
