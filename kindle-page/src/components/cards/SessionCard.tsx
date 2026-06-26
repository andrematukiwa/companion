import { CardEvent } from '../../types/card'

export function SessionCard({ card }: { card: CardEvent }) {
  const focusMinutes = card.content.focusMinutes as number
  const pomodorosToday = card.content.pomodorosToday as number
  const project = card.content.project as string

  const hours = Math.floor(focusMinutes / 60)
  const minutes = focusMinutes % 60
  const focusLabel = hours > 0 ? `${hours}h ${minutes}min` : `${minutes}min`

  return (
    <div style={styles.container}>
      <div style={styles.title}>Sessão</div>
      {project ? <div style={styles.project}>{project}</div> : null}
      <div style={styles.grid}>
        <Metric label="Foco" value={focusLabel} />
        <Metric label="Pomodoros" value={String(pomodorosToday)} />
      </div>
    </div>
  )
}

function Metric({ label, value }: { label: string; value: string }) {
  return (
    <div style={styles.metric}>
      <div style={styles.label}>{label}</div>
      <div style={styles.value}>{value}</div>
    </div>
  )
}

const styles: Record<string, React.CSSProperties> = {
  container: { padding: '32px 40px' },
  title: { fontSize: 18, fontWeight: 'bold', letterSpacing: 2, marginBottom: 12, textTransform: 'uppercase' },
  project: { fontSize: 16, color: '#555', fontFamily: 'monospace', marginBottom: 24 },
  grid: { display: 'flex', gap: 48 },
  metric: {},
  label: { fontSize: 13, letterSpacing: 1, textTransform: 'uppercase', color: '#555', marginBottom: 6 },
  value: { fontSize: 36, fontWeight: 'bold' },
}
