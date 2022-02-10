export class JsonScope {
  private isScopePositive: boolean;
  private scopeSet: Set<string> = new Set<string>();

  constructor(isPositive: boolean, scope: string[]) {
    this.isScopePositive = isPositive;
    for (const objectType of scope) {
      this.scopeSet.add(objectType);
    }
  }

  public isPositive(): boolean {
    return this.isScopePositive;
  }

  public getScope(): Set<string> {
    return this.scopeSet;
  }

  isInScope(idValue: string): boolean {
    const idComponents = idValue.split('/');
    if (idComponents.length !== 2) {
      return true;
    }

    if (this.getScope().has(idComponents[0])) {
      return this.isPositive();
    } else {
      return !this.isPositive();
    }
  }
}
