import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IInventory } from 'app/shared/model/inventory.model';
import { getEntities } from './inventory.reducer';

export const Inventory = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const inventoryList = useAppSelector(state => state.webshop.inventory.entities);
  const loading = useAppSelector(state => state.webshop.inventory.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="inventory-heading" data-cy="InventoryHeading">
        <Translate contentKey="webshopApp.inventory.home.title">Inventories</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="webshopApp.inventory.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/inventory/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webshopApp.inventory.home.createLabel">Create new Inventory</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {inventoryList && inventoryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="webshopApp.inventory.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="webshopApp.inventory.inventoryID">Inventory ID</Translate>
                </th>
                <th>
                  <Translate contentKey="webshopApp.inventory.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="webshopApp.inventory.quantity">Quantity</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {inventoryList.map((inventory, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/inventory/${inventory.id}`} color="link" size="sm">
                      {inventory.id}
                    </Button>
                  </td>
                  <td>{inventory.inventoryID}</td>
                  <td>{inventory.name}</td>
                  <td>{inventory.quantity}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/inventory/${inventory.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/inventory/${inventory.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/inventory/${inventory.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="webshopApp.inventory.home.notFound">No Inventories found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Inventory;
