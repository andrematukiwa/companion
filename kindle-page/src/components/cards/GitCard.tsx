import { CardEvent } from '../../types/card'

export function GitCard({ card }: { card: CardEvent }) {
  const branch = card.content.branch as string
  const ahead = card.content.ahead as number
  const changed = card.content.changed as number

  return (
    <div style={styles.container}>
      <div style={styles.title}>Git</div>
      <div style={styles.branch}>{branch}</div>
      <div style={styles.stats}>
        <Stat label="Ahead" value={ahead} />
        <Stat label="Changed" value={changed} />
      </div>
    </div>
  )
}

function Stat({ label, value }: { label: string; value: number }) {
  return (
    <div style={styles.stat}>
      <div style={styles.statLabel}>{label}</div>
      <div style={styles.statValue}>{value}</div>
    </div>
  )
}

const styles: Record<string, React.CSSProperties> = {
  container: { padding: '32px 40px' },
  title: { fontSize: 18, fontWeight: 'bold', letterSpacing: 2, marginBottom: 20, textTransform: 'uppercase' },
  branch: { fontSize: 26, fontFamily: 'monospace', marginBottom: 28, wordBreak: 'break-all' },
  stats: { display: 'flex', gap: 40 },
  stat: {},
  statLabel: { fontSize: 13, letterSpacing: 1, textTransform: 'uppercase', color: '#555', marginBottom: 4 },
  statValue: { fontSize: 36, fontWeight: 'bold' },
}
