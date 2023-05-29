import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SpotifyProfile from './spotify-profile';
import SpotifyProfileDetail from './spotify-profile-detail';
import SpotifyProfileUpdate from './spotify-profile-update';
import SpotifyProfileDeleteDialog from './spotify-profile-delete-dialog';

const SpotifyProfileRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SpotifyProfile />} />
    <Route path="new" element={<SpotifyProfileUpdate />} />
    <Route path=":id">
      <Route index element={<SpotifyProfileDetail />} />
      <Route path="edit" element={<SpotifyProfileUpdate />} />
      <Route path="delete" element={<SpotifyProfileDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SpotifyProfileRoutes;
