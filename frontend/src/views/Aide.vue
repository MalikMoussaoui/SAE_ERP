<template>
  <DashboardLayout>
    <template #header>
      <h1 class="page-title">{{ $t('help.title') }}</h1>
    </template>

    <div class="page-surface">
      <div class="help-container">
        <div class="help-hero">
          <div class="hero-icon">?</div>
          <h2>{{ $t('help.subtitle') }}</h2>
          <p>{{ $t('help.description') }}</p>
        </div>

        <div class="help-grid">
          <div class="help-card">
            <h3>{{ $t('help.faq') }}</h3>
            <ul class="faq-list">
              <li>
                <details>
                  <summary>{{ $t('help.q1') }}</summary>
                  <p>{{ $t('help.a1') }}</p>
                </details>
              </li>
              <li>
                <details>
                  <summary>{{ $t('help.q2') }}</summary>
                  <p>{{ $t('help.a2') }}</p>
                </details>
              </li>
              <li>
                <details>
                  <summary>{{ $t('help.q3') }}</summary>
                  <p>{{ $t('help.a3') }}</p>
                </details>
              </li>
            </ul>
          </div>

          <div class="help-card">
            <h3>{{ $t('help.contact') }}</h3>
            <div class="contact-item">
              <span class="icon">📧</span>
              <div>
                <strong>{{ $t('help.email') }}</strong>
                <br>
                <a href="mailto:support@gestiut.fr">support@gestiut.fr</a>
              </div>
            </div>
            <div class="contact-item">
              <span class="icon">📞</span>
              <div>
                <strong>{{ $t('help.phone') }}</strong>
                <br>
                <a href="tel:+33555434343">05 55 43 43 43</a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </DashboardLayout>
</template>

<script>
import DashboardLayout from '@/components/DashboardLayout.vue';

export default {
  name: 'AideView',
  components: { DashboardLayout }
}
</script>

<style scoped>
.page-title {
  font-family: var(--font-primary, 'Poppins', sans-serif);
  font-size: 2.4rem;
  font-weight: 700;
  color: var(--color-text-header);
  margin: 0;
}

.page-surface {
  background: var(--color-card-bg);
  border: 1px solid var(--color-border);
  border-radius: 20px;
  padding: 2rem;
  box-shadow: var(--shadow);
  min-height: 60vh;
}

.help-container {
  max-width: 900px;
  margin: 0 auto;
}

.help-hero {
  text-align: center;
  margin-bottom: 3rem;
  padding-bottom: 2rem;
  border-bottom: 1px solid var(--color-border);
}

.hero-icon {
  width: 60px;
  height: 60px;
  background-color: var(--color-primary);
  color: white;
  font-size: 2rem;
  font-weight: bold;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1rem;
  box-shadow: 0 4px 10px rgba(var(--color-primary-rgb), 0.3);
}

.help-hero h2 { margin: 0.5rem 0; color: var(--color-text-header); }
.help-hero p { color: var(--color-text-body); font-size: 1.1rem; }

.help-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.help-card {
  background: var(--color-sidebar-bg);
  padding: 1.5rem;
  border-radius: 15px;
  border: 1px solid var(--color-border);
}

.help-card h3 { margin-top: 0; color: var(--color-primary); border-bottom: 2px solid var(--color-border); padding-bottom: 0.5rem; margin-bottom: 1rem; }

.faq-list { list-style: none; padding: 0; }
.faq-list li { margin-bottom: 1rem; }

details summary { cursor: pointer; font-weight: 600; color: var(--color-text-header); padding: 0.5rem 0; }
details p { margin: 0.5rem 0 0; color: var(--color-text-body); padding-left: 1rem; border-left: 2px solid var(--color-border); }

.contact-item { display: flex; align-items: center; gap: 1rem; margin-bottom: 1.5rem; background: var(--color-card-bg); padding: 1rem; border-radius: 10px; border: 1px solid var(--color-border); }
.contact-item .icon { font-size: 1.5rem; }
.contact-item a { color: var(--color-primary); text-decoration: none; font-weight: 600; }
.contact-item a:hover { text-decoration: underline; }
</style>