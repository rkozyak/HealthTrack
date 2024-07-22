import { defineConfig } from 'astro/config'
import react from '@astrojs/react'
import tailwind from '@astrojs/tailwind'

// https://astro.build/config
export default defineConfig({
  site: 'https://rkozyak.github.io/',
  base: '/CS2340A_Team6/',
  integrations: [
    react(),
    tailwind({
      applyBaseStyles: false,
    }),
  ],
})
