export class Arrays {
  public static isNullOrEmpty<T>(array: T[]): boolean {
    if (!array) {
      return true;
    }
    return array.length === 0;
  }
}