/** Defining extended method for global types */
declare interface String {
  isEmpty(): boolean;

  charArray(): string[];

  characterLength(): number;

  containsUnicode(): boolean;
}

declare interface  Array<T> {
  lastElement(): T;

  isEmpty(): boolean;
}
