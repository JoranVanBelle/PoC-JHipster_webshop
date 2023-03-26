import customer from 'app/entities/customer/customer.reducer';
import inventory from 'app/entities/inventory/inventory.reducer';
import order from 'app/entities/order/order.reducer';
import pricing from 'app/entities/pricing/pricing.reducer';
import transport from 'app/entities/transport/transport.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  customer,
  inventory,
  order,
  pricing,
  transport,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
