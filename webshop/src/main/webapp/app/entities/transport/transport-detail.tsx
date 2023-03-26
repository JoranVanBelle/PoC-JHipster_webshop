import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './transport.reducer';

export const TransportDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const transportEntity = useAppSelector(state => state.webshop.transport.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="transportDetailsHeading">
          <Translate contentKey="webshopApp.transport.detail.title">Transport</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{transportEntity.id}</dd>
          <dt>
            <span id="transportID">
              <Translate contentKey="webshopApp.transport.transportID">Transport ID</Translate>
            </span>
          </dt>
          <dd>{transportEntity.transportID}</dd>
          <dt>
            <span id="transportName">
              <Translate contentKey="webshopApp.transport.transportName">Transport Name</Translate>
            </span>
          </dt>
          <dd>{transportEntity.transportName}</dd>
        </dl>
        <Button tag={Link} to="/transport" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/transport/${transportEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TransportDetail;
