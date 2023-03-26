import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pricing.reducer';

export const PricingDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pricingEntity = useAppSelector(state => state.webshop.pricing.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pricingDetailsHeading">
          <Translate contentKey="webshopApp.pricing.detail.title">Pricing</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pricingEntity.id}</dd>
          <dt>
            <span id="pricingID">
              <Translate contentKey="webshopApp.pricing.pricingID">Pricing ID</Translate>
            </span>
          </dt>
          <dd>{pricingEntity.pricingID}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="webshopApp.pricing.name">Name</Translate>
            </span>
          </dt>
          <dd>{pricingEntity.name}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="webshopApp.pricing.price">Price</Translate>
            </span>
          </dt>
          <dd>{pricingEntity.price}</dd>
        </dl>
        <Button tag={Link} to="/pricing" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pricing/${pricingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PricingDetail;
