class Card(object):
    def __init__(self, identifier=None, card_id="", short_code=""):
        self.identifier = identifier
        self.card_id = card_id
        self.short_code = short_code

    def __repr__(self):
        return f"<Card {self.identifier}>"
