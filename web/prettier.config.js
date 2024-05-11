module.exports = {
  singleQuote: true,
  bracketSpacing: false,
  trailingComma: 'all',
  printWidth: 120,
  tabWidth: 2,
  importOrder: [
    'react(?=[a-zA-Z\\-\\/]+$)*',
    '^[a-zA-Z\\-\\/]+$',
    '^@resources$',
    '^@specials\\/',
    '^@\\/([a-zA-Z\\/-]*)$',
    '^(\\.{1,2}\\/)+[a-zA-Z\\/]+$',
    '.css$',
  ],
  importOrderSeparation: true,
  importOrderSortSpecifiers: true,
};
