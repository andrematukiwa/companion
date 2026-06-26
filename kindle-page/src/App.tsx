import { useSSE } from './hooks/useSSE'
import { KindleDisplay } from './components/KindleDisplay'

export function App() {
  const token = new URLSearchParams(window.location.search).get('token')
  const { status, currentCard } = useSSE(token)

  return <KindleDisplay status={status} currentCard={currentCard} />
}
