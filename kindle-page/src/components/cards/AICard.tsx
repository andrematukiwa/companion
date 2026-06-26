import { CardEvent } from '../../types/card'

export function AICard({ card }: { card: CardEvent }) {
  const topic = card.content.topic as string
  const insight = card.content.insight as string

  return (
    <div style={styles.container}>
      <div style={styles.label}>Insight do Dia</div>
      <div style={styles.topic}>{topic}</div>
      <div style={styles.divider} />
      <div style={styles.insight}>{insight}</div>
    </div>
  )
}

const styles: Record<string, React.CSSProperties> = {
  container: { padding: '32px 40px' },
  label: { fontSize: 18, fontWeight: 'bold', letterSpacing: 2, marginBottom: 20, textTransform: 'uppercase' },
  topic: { fontSize: 28, fontWeight: 'bold', marginBottom: 16, lineHeight: 1.3 },
  divider: { borderTop: '1px solid #ddd', marginBottom: 18 },
  insight: { fontSize: 17, lineHeight: 1.8, color: '#333', fontFamily: 'Georgia, serif' },
}
