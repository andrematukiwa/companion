import { CardEvent } from '../../types/card'

export function BuildCard({ card }: { card: CardEvent }) {
  const status = card.content.status as string
  const step = card.content.step as string
  const project = card.content.project as string
  const durationMs = card.content.durationMs as number

  const failed = status === 'FAILED'

  return (
    <div style={{ ...styles.container, borderLeft: failed ? '6px solid #000' : 'none', paddingLeft: failed ? 34 : 40 }}>
      <div style={styles.title}>Build</div>
      <div style={{ ...styles.status, fontSize: failed ? 42 : 36 }}>{statusLabel(status)}</div>
      {project ? <div style={styles.meta}>{project}</div> : null}
      {step ? <div style={styles.meta}>{step}</div> : null}
      {durationMs > 0 ? <div style={styles.duration}>{(durationMs / 1000).toFixed(1)}s</div> : null}
    </div>
  )
}

function statusLabel(status: string) {
  switch (status) {
    case 'SUCCESS': return 'OK'
    case 'FAILED':  return 'FALHOU'
    case 'RUNNING': return 'Rodando...'
    default:        return status
  }
}

const styles: Record<string, React.CSSProperties> = {
  container: { padding: '32px 40px' },
  title: { fontSize: 18, fontWeight: 'bold', letterSpacing: 2, marginBottom: 20, textTransform: 'uppercase' },
  status: { fontWeight: 'bold', marginBottom: 16 },
  meta: { fontSize: 16, color: '#555', marginBottom: 6, fontFamily: 'monospace' },
  duration: { fontSize: 14, color: '#999', marginTop: 12 },
}
