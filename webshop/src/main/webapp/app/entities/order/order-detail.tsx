import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './order.reducer';

export const OrderDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const orderEntity = useAppSelector(state => state.webshop.order.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderDetailsHeading">
          <Translate contentKey="webshopApp.order.detail.title">Order</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{orderEntity.id}</dd>
          <dt>
            <span id="orderID">
              <Translate contentKey="webshopApp.order.orderID">Order ID</Translate>
            </span>
          </dt>
          <dd>{orderEntity.orderID}</dd>
          <dt>
            <span id="orderPrice">
              <Translate contentKey="webshopApp.order.orderPrice">Order Price</Translate>
            </span>
          </dt>
          <dd>{orderEntity.orderPrice}</dd>
          <dt>
            <span id="orderQuantity">
              <Translate contentKey="webshopApp.order.orderQuantity">Order Quantity</Translate>
            </span>
          </dt>
          <dd>{orderEntity.orderQuantity}</dd>
          <dt>
            <span id="username">
              <Translate contentKey="webshopApp.order.username">Username</Translate>
            </span>
          </dt>
          <dd>{orderEntity.username}</dd>
          <dt>
            <span id="transportID">
              <Translate contentKey="webshopApp.order.transportID">Transport ID</Translate>
            </span>
          </dt>
          <dd>{orderEntity.transportID}</dd>
        </dl>
        <Button tag={Link} to="/order" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order/${orderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrderDetail;
