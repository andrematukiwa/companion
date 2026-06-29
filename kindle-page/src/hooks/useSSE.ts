import { useState, useEffect, useRef } from 'react'
import { CardEvent, ConnectionStatus } from '../types/card'

const API_URL = import.meta.env.VITE_API_URL ?? ''

export function useSSE(token: string | null) {
  const [status, setStatus] = useState<ConnectionStatus>('connecting')
  const [currentCard, setCurrentCard] = useState<CardEvent | null>(null)
  const esRef = useRef<EventSource | null>(null)

  useEffect(() => {
    if (!token) {
      setStatus('unauthorized')
      return
    }

    function connect() {
      const url = `${API_URL}/api/sse/kindle?token=${token}`
      const es = new EventSource(url)
      esRef.current = es

      es.addEventListener('heartbeat', () => {
        setStatus('connected')
      })

      es.addEventListener('card', (e) => {
        setStatus('connected')
        const data = JSON.parse(e.data) as CardEvent
        setCurrentCard(data)
      })

      es.addEventListener('alert', (e) => {
        const data = JSON.parse(e.data) as CardEvent
        setCurrentCard(data)
      })

      es.addEventListener('clear', () => {
        setCurrentCard(null)
      })

      es.onerror = () => {
        setStatus('connecting')
        es.close()
        esRef.current = null
        setTimeout(connect, 5000)
      }
    }

    connect()

    return () => {
      esRef.current?.close()
    }
  }, [token])

  return { status, currentCard }
}
