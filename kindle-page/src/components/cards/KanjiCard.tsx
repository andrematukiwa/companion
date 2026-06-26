import { CardEvent } from '../../types/card'

export function KanjiCard({ card }: { card: CardEvent }) {
  const character = card.content.character as string
  const reading = card.content.reading as string
  const meaning = card.content.meaning as string
  const example = card.content.example as string
  const exampleReading = card.content.exampleReading as string

  return (
    <div style={styles.container}>
      <div style={styles.title}>Kanji do Dia</div>
      <div style={styles.character}>{character}</div>
      <div style={styles.reading}>{reading}</div>
      <div style={styles.meaning}>{meaning}</div>
      <div style={styles.divider} />
      <div style={styles.exampleRow}>
        <span style={styles.example}>{example}</span>
        <span style={styles.exampleReading}>{exampleReading}</span>
      </div>
    </div>
  )
}

const styles: Record<string, React.CSSProperties> = {
  container: { padding: '32px 40px' },
  title: { fontSize: 18, fontWeight: 'bold', letterSpacing: 2, marginBottom: 20, textTransform: 'uppercase' },
  character: { fontSize: 80, lineHeight: 1, marginBottom: 12 },
  reading: { fontSize: 18, color: '#555', marginBottom: 6 },
  meaning: { fontSize: 22, fontWeight: 'bold', marginBottom: 20 },
  divider: { borderTop: '1px solid #ddd', marginBottom: 16 },
  exampleRow: { display: 'flex', alignItems: 'baseline', gap: 12 },
  example: { fontSize: 28, fontWeight: 'bold' },
  exampleReading: { fontSize: 16, color: '#555' },
}
