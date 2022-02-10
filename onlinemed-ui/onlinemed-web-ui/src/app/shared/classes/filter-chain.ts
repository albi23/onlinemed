export class FilterChain<T> {

  validators: ((...arg: T[]) => boolean)[];

  constructor(...validators: ((...arg: T[]) => boolean)[]) {
    this.validators = validators;
  }

  addFilter(filter: (arg: T) => boolean): this {
    this.validators.push(filter);
    return this;
  }

  filter(value: T): boolean {
    for (const validator of this.validators) {
      if (!validator(value)) {
        return false;
      }
    }
    return true;
  }

  filterMany(values: T[]): boolean {
    for (const val of values) {
      if (!this.filter(val)) {
        return false;
      }
    }
    return true;
  }
}
