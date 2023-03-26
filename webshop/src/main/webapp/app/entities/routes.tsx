import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { ReducersMapObject, combineReducers } from '@reduxjs/toolkit';

import getStore from 'app/config/store';

import entitiesReducers from './reducers';

import Customer from './customer';
import Inventory from './inventory';
import Order from './order';
import Pricing from './pricing';
import Transport from './transport';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  const store = getStore();
  store.injectReducer('webshop', combineReducers(entitiesReducers as ReducersMapObject));
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="customer/*" element={<Customer />} />
        <Route path="inventory/*" element={<Inventory />} />
        <Route path="order/*" element={<Order />} />
        <Route path="pricing/*" element={<Pricing />} />
        <Route path="transport/*" element={<Transport />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
