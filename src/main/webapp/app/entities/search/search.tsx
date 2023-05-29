import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISearch } from 'app/shared/model/search.model';
import { getEntities } from './search.reducer';

export const Search = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const searchList = useAppSelector(state => state.search.entities);
  const loading = useAppSelector(state => state.search.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="search-heading" data-cy="SearchHeading">
        <Translate contentKey="betterSearchSpotifyApp.search.home.title">Searches</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="betterSearchSpotifyApp.search.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/search/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="betterSearchSpotifyApp.search.home.createLabel">Create new Search</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {searchList && searchList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="betterSearchSpotifyApp.search.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="betterSearchSpotifyApp.search.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="betterSearchSpotifyApp.search.date">Date</Translate>
                </th>
                <th>
                  <Translate contentKey="betterSearchSpotifyApp.search.createPlayist">Create Playist</Translate>
                </th>
                <th>
                  <Translate contentKey="betterSearchSpotifyApp.search.user">User</Translate>
                </th>
                <th>
                  <Translate contentKey="betterSearchSpotifyApp.search.searchParameter">Search Parameter</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {searchList.map((search, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/search/${search.id}`} color="link" size="sm">
                      {search.id}
                    </Button>
                  </td>
                  <td>{search.name}</td>
                  <td>{search.date ? <TextFormat type="date" value={search.date} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{search.createPlayist ? 'true' : 'false'}</td>
                  <td>{search.user ? search.user.login : ''}</td>
                  <td>
                    {search.searchParameter ? (
                      <Link to={`/search-parameter/${search.searchParameter.id}`}>{search.searchParameter.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/search/${search.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/search/${search.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/search/${search.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="betterSearchSpotifyApp.search.home.notFound">No Searches found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Search;
