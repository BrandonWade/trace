import stream from 'mithril/stream';

class Filters {
  constructor() {
    const filters = localStorage.getItem('trace.filters.ignore') || [];
    this.filters = stream(JSON.parse(filters));
    this.newFilter = stream();
    this.selectedFilterIndex = stream();
    this.updateServer();
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
    this.updateServer();
  }

  updateServer() {
    const filters = { filters: this.filters };
    const headers = new Headers({
      'Content-Type' : 'application/json',
    });

    fetch('/update/filters', {
      method: 'POST',
      headers: headers,
      body: JSON.stringify(filters),
    });
  }
};

export default Filters;
