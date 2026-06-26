import { CardEvent } from '../../types/card'

export function ArchitectureCard({ card }: { card: CardEvent }) {
  const tipTitle = card.content.tipTitle as string
  const tipBody = card.content.tipBody as string

  return (
    <div style={styles.container}>
      <div style={styles.label}>Arquitetura</div>
      <div style={styles.tipTitle}>{tipTitle}</div>
      <div style={styles.tipBody}>{tipBody}</div>
    </div>
  )
}

const styles: Record<string, React.CSSProperties> = {
  container: { padding: '32px 40px' },
  label: { fontSize: 18, fontWeight: 'bold', letterSpacing: 2, marginBottom: 20, textTransform: 'uppercase' },
  tipTitle: { fontSize: 26, fontWeight: 'bold', marginBottom: 18, lineHeight: 1.3 },
  tipBody: { fontSize: 17, lineHeight: 1.7, color: '#333' },
}
