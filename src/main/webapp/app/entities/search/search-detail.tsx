import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './search.reducer';

export const SearchDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const searchEntity = useAppSelector(state => state.search.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="searchDetailsHeading">
          <Translate contentKey="betterSearchSpotifyApp.search.detail.title">Search</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{searchEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="betterSearchSpotifyApp.search.name">Name</Translate>
            </span>
          </dt>
          <dd>{searchEntity.name}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="betterSearchSpotifyApp.search.date">Date</Translate>
            </span>
          </dt>
          <dd>{searchEntity.date ? <TextFormat value={searchEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="createPlayist">
              <Translate contentKey="betterSearchSpotifyApp.search.createPlayist">Create Playist</Translate>
            </span>
          </dt>
          <dd>{searchEntity.createPlayist ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="betterSearchSpotifyApp.search.user">User</Translate>
          </dt>
          <dd>{searchEntity.user ? searchEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="betterSearchSpotifyApp.search.searchParameter">Search Parameter</Translate>
          </dt>
          <dd>{searchEntity.searchParameter ? searchEntity.searchParameter.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/search" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/search/${searchEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SearchDetail;
