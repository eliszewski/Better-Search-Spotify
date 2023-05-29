import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISpotifyProfile } from 'app/shared/model/spotify-profile.model';
import { getEntities } from './spotify-profile.reducer';

export const SpotifyProfile = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const spotifyProfileList = useAppSelector(state => state.spotifyProfile.entities);
  const loading = useAppSelector(state => state.spotifyProfile.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="spotify-profile-heading" data-cy="SpotifyProfileHeading">
        <Translate contentKey="betterSearchSpotifyApp.spotifyProfile.home.title">Spotify Profiles</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="betterSearchSpotifyApp.spotifyProfile.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/spotify-profile/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="betterSearchSpotifyApp.spotifyProfile.home.createLabel">Create new Spotify Profile</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {spotifyProfileList && spotifyProfileList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="betterSearchSpotifyApp.spotifyProfile.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="betterSearchSpotifyApp.spotifyProfile.accessToken">Access Token</Translate>
                </th>
                <th>
                  <Translate contentKey="betterSearchSpotifyApp.spotifyProfile.refreshToken">Refresh Token</Translate>
                </th>
                <th>
                  <Translate contentKey="betterSearchSpotifyApp.spotifyProfile.accessTokenExpiration">Access Token Expiration</Translate>
                </th>
                <th>
                  <Translate contentKey="betterSearchSpotifyApp.spotifyProfile.refreshTokenExpiration">Refresh Token Expiration</Translate>
                </th>
                <th>
                  <Translate contentKey="betterSearchSpotifyApp.spotifyProfile.user">User</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {spotifyProfileList.map((spotifyProfile, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/spotify-profile/${spotifyProfile.id}`} color="link" size="sm">
                      {spotifyProfile.id}
                    </Button>
                  </td>
                  <td>{spotifyProfile.accessToken}</td>
                  <td>{spotifyProfile.refreshToken}</td>
                  <td>
                    {spotifyProfile.accessTokenExpiration ? (
                      <TextFormat type="date" value={spotifyProfile.accessTokenExpiration} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {spotifyProfile.refreshTokenExpiration ? (
                      <TextFormat type="date" value={spotifyProfile.refreshTokenExpiration} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{spotifyProfile.user ? spotifyProfile.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/spotify-profile/${spotifyProfile.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/spotify-profile/${spotifyProfile.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/spotify-profile/${spotifyProfile.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="betterSearchSpotifyApp.spotifyProfile.home.notFound">No Spotify Profiles found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default SpotifyProfile;
