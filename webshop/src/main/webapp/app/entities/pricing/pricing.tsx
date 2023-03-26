import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPricing } from 'app/shared/model/pricing.model';
import { getEntities } from './pricing.reducer';

export const Pricing = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const pricingList = useAppSelector(state => state.webshop.pricing.entities);
  const loading = useAppSelector(state => state.webshop.pricing.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="pricing-heading" data-cy="PricingHeading">
        <Translate contentKey="webshopApp.pricing.home.title">Pricings</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="webshopApp.pricing.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/pricing/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webshopApp.pricing.home.createLabel">Create new Pricing</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {pricingList && pricingList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="webshopApp.pricing.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="webshopApp.pricing.pricingID">Pricing ID</Translate>
                </th>
                <th>
                  <Translate contentKey="webshopApp.pricing.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="webshopApp.pricing.price">Price</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {pricingList.map((pricing, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/pricing/${pricing.id}`} color="link" size="sm">
                      {pricing.id}
                    </Button>
                  </td>
                  <td>{pricing.pricingID}</td>
                  <td>{pricing.name}</td>
                  <td>{pricing.price}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/pricing/${pricing.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/pricing/${pricing.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/pricing/${pricing.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="webshopApp.pricing.home.notFound">No Pricings found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Pricing;
