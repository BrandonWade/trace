import React, { Component } from 'react';
import TextBox from '../../components/forms/textbox/textbox';
import Button from '../../components/forms/button/button';
import Select from '../../components/forms/select/select';

class Filters extends Component {
  constructor(props) {
    super(props);

    this.state = {
      filters: window.settings.Filters || [],
      newFilter: '',
      selectedFilterIndex: -1,
    }
  }

  updateState(value, field, callback = () => {}) {
    this.setState({
      ...this.state,
      [field]: value,
    }, callback);
  }

  add() {
    this.updateState([
      ...this.state.filters,
      this.state.newFilter,
    ], 'filters', this.save);
  }

  remove() {
    if (this.state.selectedFilterIndex !== -1 && this.state.filters.length > 0) {
      this.updateState([
        ...this.state.filters.slice(0, this.state.selectedFilterIndex),
        ...this.state.filters.slice(this.state.selectedFilterIndex + 1),
      ], 'filters', this.save);
    }
  }

  save() {
    const filters = { filters: this.state.filters };
    const headers = new Headers({
      'Content-Type' : 'application/json',
    });

    fetch('/settings/update/filters', {
      method: 'POST',
      headers: headers,
      body: JSON.stringify(filters),
    });
  }

  render() {
    return (
      <div className={ 'FiltersPage' }>
        <h1 className={ 'Page-Heading' }>Filters</h1>
        <section className={ 'Page-Section' }>
          <TextBox description={ 'New filter:' } handleChange={ e => this.updateState(e.target.value, 'newFilter') } />
          <Button value={ 'Add' } handleClick={ () => this.add() } />
        </section>
        <section className={ 'Page-Section' }>
          <Select description={ 'Existing filters:' } options={ this.state.filters } handleChange={ e => this.updateState(e.target.selectedIndex, 'selectedFilterIndex') } multiple={ this.props.multiple } />
          <Button value={ 'Remove' } handleClick={ () => this.remove() } />
        </section>
      </div>
    );
  }
};

export default Filters;
