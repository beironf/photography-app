export const nextIndex = (current: number, carouselLength: number): number => {
  if (current === -1) return -1;
  if (current === carouselLength - 1) return 0;
  return current + 1;
};

export const prevIndex = (current: number, carouselLength: number): number => {
  if (current === -1) return -1;
  if (current === 0) return carouselLength - 1;
  return current - 1;
};
