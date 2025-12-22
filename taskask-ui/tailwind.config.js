module.exports = {
  content: ['./src/**/*.{html,ts}'],
  theme: {
    extend: {
      colors: {
        base: {
          50: '#f8fbff',
          100: '#eef4ff',
          200: '#d6e4ff',
          300: '#adc8ff',
          400: '#84a9ff',
          500: '#5a8bff',
          600: '#3c6de6',
          700: '#2854bf',
          800: '#1d3b8c',
          900: '#12275d'
        },
        accent: '#ffb347',
        positive: '#22c55e',
        warning: '#f97316',
        danger: '#ef4444',
        surface: '#0f1521'
      },
      fontFamily: {
        sans: ['"Space Grotesk"', 'ui-sans-serif', 'system-ui']
      },
      boxShadow: {
        card: '0 10px 30px -15px rgba(15, 21, 33, 0.45)'
      }
    }
  },
  plugins: []
};