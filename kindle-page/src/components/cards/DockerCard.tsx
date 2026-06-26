import { CardEvent } from '../../types/card'

interface Service { name: string; status: string }

export function DockerCard({ card }: { card: CardEvent }) {
  const services = (card.content.services ?? []) as Service[]

  return (
    <div style={styles.container}>
      <div style={styles.title}>Docker</div>
      <div style={styles.list}>
        {services.map(s => (
          <div key={s.name} style={styles.row}>
            <span style={styles.name}>{s.name}</span>
            <span style={{ ...styles.badge, background: badgeColor(s.status) }}>
              {s.status}
            </span>
          </div>
        ))}
      </div>
    </div>
  )
}

function badgeColor(status: string) {
  if (status === 'online') return '#000'
  if (status === 'slow') return '#555'
  return '#aaa'
}

const styles: Record<string, React.CSSProperties> = {
  container: { padding: '32px 40px' },
  title: { fontSize: 18, fontWeight: 'bold', letterSpacing: 2, marginBottom: 24, textTransform: 'uppercase' },
  list: { display: 'flex', flexDirection: 'column', gap: 14 },
  row: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '1px solid #ddd', paddingBottom: 10 },
  name: { fontSize: 20 },
  badge: { color: '#fff', fontSize: 13, padding: '3px 10px', borderRadius: 4 },
}
