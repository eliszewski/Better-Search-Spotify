import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SearchParameter from './search-parameter';
import SearchParameterDetail from './search-parameter-detail';
import SearchParameterUpdate from './search-parameter-update';
import SearchParameterDeleteDialog from './search-parameter-delete-dialog';

const SearchParameterRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SearchParameter />} />
    <Route path="new" element={<SearchParameterUpdate />} />
    <Route path=":id">
      <Route index element={<SearchParameterDetail />} />
      <Route path="edit" element={<SearchParameterUpdate />} />
      <Route path="delete" element={<SearchParameterDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SearchParameterRoutes;
