import { CardEvent } from '../../types/card'

export function CpuCard({ card }: { card: CardEvent }) {
  const cpu = card.content.cpuPercent as number
  const used = card.content.ramUsedMb as number
  const total = card.content.ramTotalMb as number

  return (
    <div style={styles.container}>
      <div style={styles.title}>System</div>
      <div style={styles.grid}>
        <Metric label="CPU" value={`${cpu}%`} />
        <Metric label="RAM" value={`${used} / ${total} MB`} />
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
  title: { fontSize: 18, fontWeight: 'bold', letterSpacing: 2, marginBottom: 28, textTransform: 'uppercase' },
  grid: { display: 'flex', flexDirection: 'column', gap: 24 },
  metric: { borderBottom: '1px solid #ddd', paddingBottom: 16 },
  label: { fontSize: 14, letterSpacing: 1, textTransform: 'uppercase', color: '#555', marginBottom: 6 },
  value: { fontSize: 32, fontWeight: 'bold' },
}
