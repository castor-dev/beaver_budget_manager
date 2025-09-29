import { useEffect, useState } from 'react'
import { api } from './services/api'

export default function App() {
  const [health, setHealth] = useState('loading...')

  useEffect(() => {
    api.get('/api/health')
      .then(res => setHealth(res.data.status || JSON.stringify(res.data)))
      .catch(() => setHealth('backend not reachable'))
  }, [])

  return (
    <div style={{ fontFamily: 'system-ui, -apple-system, Segoe UI, Roboto, Arial', padding: 24 }}>
      <h1>Beaver Budget Manager</h1>
      <p>Frontend + Backend scaffold is ready.</p>
      <h2>Backend Health</h2>
      <code>{health}</code>
    </div>
  )
}
