import React, { Component } from 'react';
import TextBox from '../components/textbox/Textbox';
import Button from '../components/button/Button';
import Select from '../components/select/Select';

class Filters extends Component {
    constructor(props) {
        super(props);

        this.state = {
            filters: window.settings.Filters || [],
            newFilter: '',
            selectedFilterIndex: -1,
        };
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
            'Content-Type': 'application/json',
        });

        fetch('/settings/update/filters', {
            method: 'POST',
            headers,
            body: JSON.stringify(filters),
        });
    }

    render() {
        return (
            <div className='FiltersPage'>
                <h1 className='Page-heading'>Filters</h1>
                <section className='Page-section'>
                    <TextBox
                        description='New filter:'
                        handleChange={e => this.updateState(e.target.value, 'newFilter')}
                    />
                    <Button
                        value='Add'
                        handleClick={() => this.add()}
                    />
                </section>
                <section className='Page-section'>
                    <Select
                        description='Existing filters:'
                        options={this.state.filters}
                        multiple={this.props.multiple}
                        handleChange={e => this.updateState(e.target.selectedIndex, 'selectedFilterIndex')}
                    />
                    <Button
                        value='Remove'
                        handleClick={() => this.remove()}
                    />
                </section>
            </div>
        );
    }
}

export default Filters;
