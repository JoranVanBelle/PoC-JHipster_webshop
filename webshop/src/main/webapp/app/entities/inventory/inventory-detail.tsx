import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './inventory.reducer';

export const InventoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const inventoryEntity = useAppSelector(state => state.webshop.inventory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="inventoryDetailsHeading">
          <Translate contentKey="webshopApp.inventory.detail.title">Inventory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{inventoryEntity.id}</dd>
          <dt>
            <span id="inventoryID">
              <Translate contentKey="webshopApp.inventory.inventoryID">Inventory ID</Translate>
            </span>
          </dt>
          <dd>{inventoryEntity.inventoryID}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="webshopApp.inventory.name">Name</Translate>
            </span>
          </dt>
          <dd>{inventoryEntity.name}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="webshopApp.inventory.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{inventoryEntity.quantity}</dd>
        </dl>
        <Button tag={Link} to="/inventory" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/inventory/${inventoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default InventoryDetail;
