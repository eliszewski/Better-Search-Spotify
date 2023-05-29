import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Search from './search';
import SpotifyProfile from './spotify-profile';
import Music from './music';
import SearchParameter from './search-parameter';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="search/*" element={<Search />} />
        <Route path="spotify-profile/*" element={<SpotifyProfile />} />
        <Route path="music/*" element={<Music />} />
        <Route path="search-parameter/*" element={<SearchParameter />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
