#ifdef __cplusplus
extern "C" {
#endif

static u32 ceu_pm_state = 0;    // TODO: max 32 devices

void ceu_pm_init (void);

void ceu_pm_sleep (void);

int ceu_pm_get (int dev) {
    return bitRead(ceu_pm_state, dev);
}

void ceu_pm_set (u8 dev, bool v) {
    if (v) {
        bitSet(ceu_pm_state, dev);
    } else {
        bitClear(ceu_pm_state, dev);
    }
}

#ifdef __cplusplus
}
#endif
