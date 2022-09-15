export class Random {
  public static element<T>(array: T[]): T {
    return array[Math.floor(Math.random() * array.length)];
  }

  public static between(min: number, max: number): number {
    return Math.floor(Math.random() * (max - min + 1)) + min;
  }
}