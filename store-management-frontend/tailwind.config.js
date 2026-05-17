/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}"
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          10: '#FFF7FA',
          50: '#FBEAF0',
          100: '#F4C0D1',
          200: '#E8909F',
          400: '#C97A8C',
          600: '#A85A6E',
          800: '#7A3A4E',
          900: '#3D2E35',
        },
        gold: { 400: '#D4A96A' },
        cream: '#F9F5F2',
      },
      fontFamily: {
        serif: ['Playfair Display', 'Georgia', 'serif'],
        sans: ['Inter', 'system-ui', 'sans-serif'],
      }
    }
  },
  plugins: [],
}