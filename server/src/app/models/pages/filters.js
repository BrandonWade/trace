import stream from 'mithril/stream';

class Filters {
  constructor() {
    const filters = JSON.parse(localStorage.getItem('trace.filters.ignore')) || [];
    this.filters = stream(filters);
    this.newFilter = stream();
    this.selectedFilterIndex = stream();
  }

  add() {
    this.filters().push(this.newFilter());
    this.save();
  }

  remove() {
    if (this.selectedFilterIndex() !== undefined && this.filters().length > 0) {
      this.filters().splice(this.selectedFilterIndex(), 1);
      this.save();
    }
  }

  save() {
    const filters = JSON.stringify(this.filters());
    localStorage.setItem('trace.filters.ignore', filters);
    fetch('/update/filters', {
      method: 'POST',
      body: filters,
    });
  }
};

export default Filters;
