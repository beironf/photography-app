export function formatDate(date: Date): string {
  const month = (date.getMonth() < 9 ? '0' : '') + (date.getMonth() + 1);
  const day = (date.getDate() < 9 ? '0' : '') + date.getDate();
  const year = date.getFullYear();

  return `${[year, month, day].join('-')} ${date.toLocaleTimeString()}`;
}
