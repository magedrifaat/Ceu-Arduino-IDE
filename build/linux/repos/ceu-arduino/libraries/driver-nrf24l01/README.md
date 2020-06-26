# NRF24L01

## API

### Includes

```
#include "nrf24l01.ceu"
```

### Macros

#### NRF24L01_IRQ

Sets the input event for radio interrupts.

```
#define NRF24L01_IRQ <fill-with-input-pin>
```

### Data Abstractions

#### NRF24L01_Data

Radio properties.

```
data NRF24L01_Data with
    var&   Lock spi;
    var    int  ce;
    var    int  csn;
    var    u8   payload_size = 32;

    // private
    var    byte config;
    var    u64  pipe0;
    event  none ok;
    event  none rx;
    event  none tx;
    event  none max;
end
```

### Code Abstractions

#### NRF24L01_Init

Initializes the radio.

```
code/await NRF24L01_Init (var& NRF24L01_Data nrf) -> NEVER;
```

#### NRF24L01_Rx

Receives data in the given buffer.

```
code/await NRF24L01_Rx (var& NRF24L01_Data nrf, var&[] byte buf) -> u8;
```

Parameters:

- `&NRF24L01_Data`: radio properties
- `&[] byte`:       reference to buffer to receive

Return:

- `u8`: id of the transmission end

The maximum size of the buffer indicates the number of bytes to receive.

#### NRF24L01_Tx

Transmits data in the given buffer. The data received from the transceiver is stored in the buffer in-place (the old data is replaced with the data received).

```
code/await NRF24L01_Tx (var& NRF24L01_Data nrf, var&[] byte buf) -> bool;
```

Parameters:

- `&NRF24L01_Data`: radio properties
- `&[] byte`:       reference to buffer to transmit

Return:

- `bool`: if the transmission was successful

## Examples

### Data transmission

Transmits an increasing byte every second:

```
#include "int0.ceu"
#include "spi.ceu"

#define NRF24L01_IRQ INT0
#include "nrf24l01.ceu"

var Lock spi = _;
var NRF24L01_Data nrf = val NRF24L01_Data(&spi, 8,7, _,_,_,_,_,_,_);
spawn NRF24L01_Init(&nrf);
await nrf.ok;

var int i;
loop i do
    await 1s;
    var[1] byte buf = [i];
    await NRF24L01_Tx(&nrf, &buf);
end
```

### Data receive

Receives incoming bytes one at a time:

```
#include "out.ceu"
#include "int0.ceu"
#include "spi.ceu"

#define NRF24L01_IRQ INT0
#include "nrf24l01.ceu"

var Lock spi = _;
var NRF24L01_Data nrf = val NRF24L01_Data(&spi, 8,7, _,_,_,_,_,_,_);
spawn NRF24L01_Init(&nrf);
await nrf.ok;

output high/low OUT_13;

loop do
    var[1] byte buf = [];
    await NRF24L01_Rx(&nrf, &buf);
    emit OUT_13((buf[0] % 2) as high/low);
end
```
