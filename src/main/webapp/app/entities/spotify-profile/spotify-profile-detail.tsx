import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './spotify-profile.reducer';

export const SpotifyProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const spotifyProfileEntity = useAppSelector(state => state.spotifyProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="spotifyProfileDetailsHeading">
          <Translate contentKey="betterSearchSpotifyApp.spotifyProfile.detail.title">SpotifyProfile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{spotifyProfileEntity.id}</dd>
          <dt>
            <span id="accessToken">
              <Translate contentKey="betterSearchSpotifyApp.spotifyProfile.accessToken">Access Token</Translate>
            </span>
          </dt>
          <dd>{spotifyProfileEntity.accessToken}</dd>
          <dt>
            <span id="refreshToken">
              <Translate contentKey="betterSearchSpotifyApp.spotifyProfile.refreshToken">Refresh Token</Translate>
            </span>
          </dt>
          <dd>{spotifyProfileEntity.refreshToken}</dd>
          <dt>
            <span id="accessTokenExpiration">
              <Translate contentKey="betterSearchSpotifyApp.spotifyProfile.accessTokenExpiration">Access Token Expiration</Translate>
            </span>
          </dt>
          <dd>
            {spotifyProfileEntity.accessTokenExpiration ? (
              <TextFormat value={spotifyProfileEntity.accessTokenExpiration} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="refreshTokenExpiration">
              <Translate contentKey="betterSearchSpotifyApp.spotifyProfile.refreshTokenExpiration">Refresh Token Expiration</Translate>
            </span>
          </dt>
          <dd>
            {spotifyProfileEntity.refreshTokenExpiration ? (
              <TextFormat value={spotifyProfileEntity.refreshTokenExpiration} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="betterSearchSpotifyApp.spotifyProfile.user">User</Translate>
          </dt>
          <dd>{spotifyProfileEntity.user ? spotifyProfileEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/spotify-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/spotify-profile/${spotifyProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SpotifyProfileDetail;
