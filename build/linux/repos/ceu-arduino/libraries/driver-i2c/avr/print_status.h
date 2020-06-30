#define s(i) Serial.println(i)

void print_stat(int x){
  switch(x){
  	case 0x08: s("TW_START");
            break;
 
  case 0x10: s("TW_REP_START");
              break;

  case 0x18: s("TW_MT_SLA_ACK");
              break;

  case 0x20: s("TW_MT_SLA_NACK");
              break;

  case 0x28: s("TW_MT_DATA_ACK");
              break;

  case 0x30: s("TW_MT_DATA_NACK");
              break;

  case 0x38: s("TW_MT_ARB_LOST");
              break;

  case 0x40: s("TW_MR_SLA_ACK");
              break;

  case 0x48: s("TW_MR_SLA_NACK");
              break;

  case 0x50: s("TW_MR_DATA_ACK");
              break;

  case 0x58: s("TW_MR_DATA_NACK");
              break;

  case 0xA8: s("TW_ST_SLA_ACK");
              break;

  case 0xB0: s("TW_ST_ARB_LOST_SLA_ACK");
              break;

  case 0xB8: s("TW_ST_DATA_ACK");
              break;

  case 0xC0: s("TW_ST_DATA_NACK");
              break;

  case 0xC8: s("TW_ST_LAST_DATA");
              break;

  case 0x60: s("TW_SR_SLA_ACK");
              break;

  case 0x68: s("TW_SR_ARB_LOST_SLA_ACK");
              break;

  case 0x70: s("TW_SR_GCALL_ACK");
              break;

  case 0x78: s("TW_SR_ARB_LOST_GCALL_ACK");
              break;

  case 0x80: s("TW_SR_DATA_ACK");
              break;

  case 0x88: s("TW_SR_DATA_NACK");
              break;

  case 0x90: s("TW_SR_GCALL_DATA_ACK");
              break;

  case 0x98: s("TW_SR_GCALL_DATA_NACK");
              break;

  case 0xA0: s("TW_SR_STOP");
              break;

  case 0xF8: s("TW_NO_INFO");
              break;

  case 0x00: s("TW_BUS_ERROR");
              break;
  }
}