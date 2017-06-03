import React, { Component } from 'react';
import TextBox from '../../forms/textbox/textbox';
import Button from '../../forms/button/button';
import Select from '../../forms/select/select';

class Filters extends Component {
  constructor(props) {
    super(props);

    this.state = {
      filters: window.settings.Filters || [],
      newFilter: '',
      selectedFilterIndex: -1,
    }
  }

  updateState(value, field) {
    this.setState({
      ...this.state,
      field: value,
    });
  }

  add() {
    this.updateState([
      ...this.state.filters,
      this.state.newFilter,
    ], 'filters');
    this.save();
  }

  remove() {
    if (this.state.selectedFilterIndex !== -1 && this.filters.length > 0) {
      this.updateState([
        ...this.state.filters.slice(0, this.state.selectedFilterIndex),
        ...this.state.filters.slice(this.state.selectedFilterIndex + 1),
      ], 'filters');
      this.save();
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
        <TextBox description={ 'New filter:' } handleChange={ e => this.updateState(e.target.value, 'newFilter') } />
        <Button value={ 'Add' } handleClick={ () => this.add() } />
        <Select description={ 'Existing filters:' } options={ this.state.filters } handleChange={ e => this.updateState(e.target.selectedIndex, 'selectedFilterIndex') } />
        <Button value={ 'Remove' } handleClick={ () => this.remove() } />
      </div>
    );
  }
};

export default Filters;
