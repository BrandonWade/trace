class Filters {
  constructor() {
    this.filters = window.settings.Filters || [];
    this.newFilter = '';
    this.selectedFilterIndex = -1;
  }

  add() {
    this.filters.push(this.newFilter);
    this.save();
  }

  remove() {
    if (this.selectedFilterIndex !== -1 && this.filters.length > 0) {
      this.filters.splice(this.selectedFilterIndex, 1);
      this.save();
    }
  }

  save() {
    const filters = { filters: this.filters };
    const headers = new Headers({
      'Content-Type' : 'application/json',
    });

    fetch('/settings/update/filters', {
      method: 'POST',
      headers: headers,
      body: JSON.stringify(filters),
    });
  }
};

export default Filters;
