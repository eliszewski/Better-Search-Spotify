import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './music.reducer';

export const MusicDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const musicEntity = useAppSelector(state => state.music.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="musicDetailsHeading">
          <Translate contentKey="betterSearchSpotifyApp.music.detail.title">Music</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{musicEntity.id}</dd>
          <dt>
            <span id="artist">
              <Translate contentKey="betterSearchSpotifyApp.music.artist">Artist</Translate>
            </span>
          </dt>
          <dd>{musicEntity.artist}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="betterSearchSpotifyApp.music.title">Title</Translate>
            </span>
          </dt>
          <dd>{musicEntity.title}</dd>
          <dt>
            <span id="album">
              <Translate contentKey="betterSearchSpotifyApp.music.album">Album</Translate>
            </span>
          </dt>
          <dd>{musicEntity.album}</dd>
          <dt>
            <span id="uri">
              <Translate contentKey="betterSearchSpotifyApp.music.uri">Uri</Translate>
            </span>
          </dt>
          <dd>{musicEntity.uri}</dd>
          <dt>
            <span id="releasedYear">
              <Translate contentKey="betterSearchSpotifyApp.music.releasedYear">Released Year</Translate>
            </span>
          </dt>
          <dd>{musicEntity.releasedYear}</dd>
          <dt>
            <span id="externalUrl">
              <Translate contentKey="betterSearchSpotifyApp.music.externalUrl">External Url</Translate>
            </span>
          </dt>
          <dd>{musicEntity.externalUrl}</dd>
          <dt>
            <span id="explicit">
              <Translate contentKey="betterSearchSpotifyApp.music.explicit">Explicit</Translate>
            </span>
          </dt>
          <dd>{musicEntity.explicit ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="betterSearchSpotifyApp.music.search">Search</Translate>
          </dt>
          <dd>{musicEntity.search ? musicEntity.search.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/music" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/music/${musicEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MusicDetail;
