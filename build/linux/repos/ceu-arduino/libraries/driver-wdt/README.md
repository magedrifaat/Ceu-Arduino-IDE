# Watchdog Timer (WDT)

*NOTE: Only a single WDT can be active at anytime.*

### Includes

```
#include "wdt.ceu"
```

### Code Abstractions

#### Wdt

Sleeps for the specified number of milliseconds.

```
code/await Wdt (var int ms) -> none;
```

Parameters:

- `int`: the number of milliseconds to sleep

Return:

- `none`

## Examples

### Periodic sleep

Blinks the LED connected to `OUT_13` periodically:

```
#include "out.ceu"
#include "wdt.ceu"

output high/low OUT_13;

loop do
    emit OUT_13(high);
    await Wdt(5000);
    emit OUT_13(low);
    await Wdt(200);
end
```
