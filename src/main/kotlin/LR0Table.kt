class LR0Table(val rows: Array<LR0Row>)

class LR0Row(val action: String, val symbolToState: Map<String, Int>)
