package fr.galaxyoyo.rp.client.gui;

import fr.galaxyoyo.rp.Character;
import fr.galaxyoyo.rp.Disciplin;
import fr.galaxyoyo.rp.packets.PacketManager;
import fr.galaxyoyo.rp.packets.PacketMixNewDisciplin;
import fr.galaxyoyo.rp.packets.PacketMixNewSpecial;
import fr.galaxyoyo.rp.packets.PacketMixSendModification;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("ALL")
public class Sheet extends AbstractController
{
	private Character c;

	@FXML
	private TextField name;

	@FXML
	private TextArea desc;

	@FXML
	private Spinner<Number> power;

	@FXML
	private Spinner<Number> toughness;

	@FXML
	private Spinner<Number> speed;

	@FXML
	private Spinner<Number> prec;

	@FXML
	private Spinner<Number> resistance;

	@FXML
	private Spinner<Number> charism;

	@FXML
	private Spinner<Number> perception;

	@FXML
	private Spinner<Number> physicPower;

	@FXML
	private Spinner<Number> psychicPower;

	@FXML
	private Spinner<Number> elementaryPower;

	@FXML
	private GridPane physicDisciplins;

	@FXML
	private GridPane mentalDisciplins;

	@FXML
	private Spinner<Number> courage;

	@FXML
	private Spinner<Number> lachete;

	@FXML
	private Spinner<Number> style;

	@FXML
	private Spinner<Number> sauvagerie;

	@FXML
	private Spinner<Number> synergie;

	@FXML
	private Spinner<Number> alignment;

	@FXML
	private Spinner<Number> luck;

	@FXML
	private VBox specialAbilities;

	@FXML
	private VBox specialEffects;

	@FXML
	private TableView equipment;

	@FXML
	private TableView bag;

	@FXML
	private TextField enduranceCategory;

	@FXML
	private Spinner<Number> encombrement;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
	}

	@SuppressWarnings("unchecked")
	public void init()
	{
		name.textProperty().bindBidirectional(c.nameProperty());
		desc.textProperty().bindBidirectional(c.descProperty());
		SpinnerValueFactory f = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, 0);
		power.setValueFactory(f);
		f = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, 0);
		toughness.setValueFactory(f);
		f = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, 0);
		speed.setValueFactory(f);
		f = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, 0);
		prec.setValueFactory(f);
		f = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, 0);
		resistance.setValueFactory(f);
		f = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, 0);
		charism.setValueFactory(f);
		f = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 15, 0);
		perception.setValueFactory(f);
		f = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 15, 0);
		physicPower.setValueFactory(f);
		f = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 15, 0);
		psychicPower.setValueFactory(f);
		f = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 15, 0);
		elementaryPower.setValueFactory(f);
		power.getValueFactory().valueProperty().bindBidirectional(c.powerProperty());
		toughness.getValueFactory().valueProperty().bindBidirectional(c.toughnessProperty());
		speed.getValueFactory().valueProperty().bindBidirectional(c.speedProperty());
		prec.getValueFactory().valueProperty().bindBidirectional(c.precProperty());
		resistance.getValueFactory().valueProperty().bindBidirectional(c.resistanceProperty());
		charism.getValueFactory().valueProperty().bindBidirectional(c.charismProperty());
		perception.getValueFactory().valueProperty().bindBidirectional(c.perceptionProperty());
		physicPower.getValueFactory().valueProperty().bindBidirectional(c.physicPowerProperty());
		psychicPower.getValueFactory().valueProperty().bindBidirectional(c.psychicPowerProperty());
		elementaryPower.getValueFactory().valueProperty().bindBidirectional(c.elementaryPowerProperty());

		f = power.getValueFactory();
		((SpinnerValueFactory.IntegerSpinnerValueFactory) f).maxProperty().bind(Bindings
				.createIntegerBinding(() -> 31 - toughness.getValue().intValue() - speed.getValue().intValue() - prec.getValue().intValue() - resistance.getValue().intValue(),
						toughness.valueProperty(), speed.valueProperty(), prec.valueProperty(), resistance.valueProperty()));

		f = toughness.getValueFactory();
		((SpinnerValueFactory.IntegerSpinnerValueFactory) f).maxProperty().bind(Bindings
				.createIntegerBinding(() -> 31 - power.getValue().intValue() - speed.getValue().intValue() - prec.getValue().intValue() - resistance.getValue().intValue(),
						power.valueProperty(), speed.valueProperty(), prec.valueProperty(), resistance.valueProperty()));

		f = speed.getValueFactory();
		((SpinnerValueFactory.IntegerSpinnerValueFactory) f).maxProperty().bind(Bindings
				.createIntegerBinding(() -> 31 - toughness.getValue().intValue() - power.getValue().intValue() - prec.getValue().intValue() - resistance.getValue().intValue(),
						toughness.valueProperty(), power.valueProperty(), prec.valueProperty(), resistance.valueProperty()));

		f = prec.getValueFactory();
		((SpinnerValueFactory.IntegerSpinnerValueFactory) f).maxProperty().bind(Bindings
				.createIntegerBinding(() -> 31 - toughness.getValue().intValue() - speed.getValue().intValue() - power.getValue().intValue() - resistance.getValue().intValue(),
						toughness.valueProperty(), speed.valueProperty(), power.valueProperty(), resistance.valueProperty()));

		f = resistance.getValueFactory();
		((SpinnerValueFactory.IntegerSpinnerValueFactory) f).maxProperty().bind(Bindings
				.createIntegerBinding(() -> 31 - toughness.getValue().intValue() - speed.getValue().intValue() - prec.getValue().intValue() - power.getValue().intValue(),
						toughness.valueProperty(), speed.valueProperty(), prec.valueProperty(), power.valueProperty()));

		f = charism.getValueFactory();
		((SpinnerValueFactory.IntegerSpinnerValueFactory) f).maxProperty().bind(Bindings
				.createIntegerBinding(
						() -> 15 - perception.getValue().intValue() - physicPower.getValue().intValue() - psychicPower.getValue().intValue() - elementaryPower.getValue().intValue(),
						perception.valueProperty(), physicPower.valueProperty(), psychicPower.valueProperty(), elementaryPower.valueProperty()));

		f = perception.getValueFactory();
		((SpinnerValueFactory.IntegerSpinnerValueFactory) f).maxProperty().bind(Bindings
				.createIntegerBinding(
						() -> 15 - charism.getValue().intValue() - physicPower.getValue().intValue() - psychicPower.getValue().intValue() - elementaryPower.getValue().intValue(),
						charism.valueProperty(), physicPower.valueProperty(), psychicPower.valueProperty(), elementaryPower.valueProperty()));

		f = physicPower.getValueFactory();
		((SpinnerValueFactory.IntegerSpinnerValueFactory) f).maxProperty().bind(Bindings
				.createIntegerBinding(
						() -> 15 - perception.getValue().intValue() - charism.getValue().intValue() - psychicPower.getValue().intValue() - elementaryPower.getValue().intValue(),
						perception.valueProperty(), charism.valueProperty(), psychicPower.valueProperty(), elementaryPower.valueProperty()));

		f = psychicPower.getValueFactory();
		((SpinnerValueFactory.IntegerSpinnerValueFactory) f).maxProperty().bind(Bindings
				.createIntegerBinding(
						() -> 15 - perception.getValue().intValue() - physicPower.getValue().intValue() - charism.getValue().intValue() - elementaryPower.getValue().intValue(),
						perception.valueProperty(), physicPower.valueProperty(), charism.valueProperty(), elementaryPower.valueProperty()));

		f = elementaryPower.getValueFactory();
		((SpinnerValueFactory.IntegerSpinnerValueFactory) f).maxProperty().bind(Bindings
				.createIntegerBinding(
						() -> 15 - perception.getValue().intValue() - physicPower.getValue().intValue() - psychicPower.getValue().intValue() - charism.getValue().intValue(),
						perception.valueProperty(), physicPower.valueProperty(), charism.valueProperty(), elementaryPower.valueProperty()));

		for (ObjectProperty<Disciplin> disciplinObjectProperty : c.getPhysicDisciplins())
			addPhysicDisciplin(disciplinObjectProperty.get());

		for (ObjectProperty<Disciplin> disciplinObjectProperty : c.getMentalDisciplins())
			addMentalDisciplin(disciplinObjectProperty.get());

		f = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 5);
		courage.setValueFactory(f);
		f = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 5);
		lachete.setValueFactory(f);
		f = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 8, 4);
		style.setValueFactory(f);
		f = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 8, 4);
		sauvagerie.setValueFactory(f);
		f = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);
		synergie.setValueFactory(f);
		f = new SpinnerValueFactory.IntegerSpinnerValueFactory(-10, 10, 0);
		alignment.setValueFactory(f);
		f = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);
		luck.setValueFactory(f);
		courage.getValueFactory().valueProperty().bindBidirectional(c.courageProperty());
		lachete.getValueFactory().valueProperty().bindBidirectional(c.lacheteProperty());
		style.getValueFactory().valueProperty().bindBidirectional(c.styleProperty());
		sauvagerie.getValueFactory().valueProperty().bindBidirectional(c.sauvagerieProperty());
		synergie.getValueFactory().valueProperty().bindBidirectional(c.synergieProperty());
		alignment.getValueFactory().valueProperty().bindBidirectional(c.alignmentProperty());
		luck.getValueFactory().valueProperty().bindBidirectional(c.luckProperty());

		AtomicBoolean modifying = new AtomicBoolean(false);

		courage.valueProperty().addListener((observableValue, number, t1) ->
		{
			if (modifying.getAndSet(true))
				return;
			lachete.getValueFactory().setValue(10 - t1.intValue());
			modifying.set(false);
		});
		lachete.valueProperty().addListener((observableValue, number, t1) ->
		{
			if (modifying.getAndSet(true))
				return;
			courage.getValueFactory().setValue(10 - t1.intValue());
			modifying.set(false);
		});

		style.valueProperty().addListener((observableValue, number, t1) ->
		{
			if (modifying.getAndSet(true))
				return;
			sauvagerie.getValueFactory().setValue(8 - t1.intValue());
			modifying.set(false);
		});
		sauvagerie.valueProperty().addListener((observableValue, number, t1) ->
		{
			if (modifying.getAndSet(true))
				return;
			style.getValueFactory().setValue(8 - t1.intValue());
			modifying.set(false);
		});

		for (StringProperty p : c.getSpecialAbilities())
			addSpecialAbility(p);

		for (StringProperty p : c.getSpecialEffects())
			addSpecialEffect(p);

		TableColumn<String, String> equip = new TableColumn<>("Équipement");
		equip.prefWidthProperty().bind(equipment.widthProperty().divide(5));
		TableColumn<String, String> name = new TableColumn<>("Nom");
		name.prefWidthProperty().bind(equipment.widthProperty().divide(5));
		TableColumn<String, String> value = new TableColumn<>("Valeur");
		value.prefWidthProperty().bind(equipment.widthProperty().divide(5));
		TableColumn<String, String> strength = new TableColumn<>("Poids");
		strength.prefWidthProperty().bind(equipment.widthProperty().divide(5));
		TableColumn<String, String> special = new TableColumn<>("Spécial");
		special.prefWidthProperty().bind(equipment.widthProperty().divide(5));
		equipment.getColumns().addAll(equip, name, value, strength, special);

		TableColumn<String, String> backpack = new TableColumn<>("Sac à dos");
		backpack.prefWidthProperty().bind(bag.widthProperty().divide(2));
		TableColumn<String, String> pocket = new TableColumn<>("Sacoche(s)");
		pocket.prefWidthProperty().bind(bag.widthProperty().divide(2));
		bag.getColumns().addAll(backpack, pocket);

		ChangeListener<Number> listener = (observableValue, number, t1) ->
		{
			int sum = c.getPower() + c.getToughness();
			String result;
			if (sum <= 2)
				result = "Très léger";
			else if (sum <= 4)
				result = "Léger";
			else if (sum <= 6)
				result = "Modérément Léger";
			else if (sum <= 8)
				result = "Stable";
			else if (sum <= 10)
				result = "Modérément Lourd";
			else if (sum <= 12)
				result = "Lourd";
			else if (sum <= 14)
				result = "Très Lourd";
			else if (sum <= 16)
				result = "Mule";
			else if (sum <= 18)
				result = "Bête de Somme";
			else if (sum <= 20)
				result = "Colosse";
			else
				result = "Titan";

			enduranceCategory.setText(result);
		};

		c.powerProperty().addListener(listener);
		c.toughnessProperty().addListener(listener);

		f = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);
		encombrement.setValueFactory(f);
		f.valueProperty().bindBidirectional(c.encombrementProperty());
	}

	public void addPhysicDisciplin(Disciplin disciplin)
	{
		int i = 0;
		for (ObjectProperty<Disciplin> p : c.getPhysicDisciplins())
		{
			if (p.get() == disciplin)
				break;
			++i;
		}

		TextField physicDName = new TextField();
		physicDName.textProperty().bindBidirectional(disciplin.nameProperty());
		physicDisciplins.getChildren().add(physicDName);
		GridPane.setRowIndex(physicDName, 2 * i + 1);
		GridPane.setRowSpan(physicDName, 2);

		ObservableList<String> physic = FXCollections.observableArrayList("---------------", "Force", "Endurance", "Rapidité", "Précision", "Résistance");

		ComboBox<String> bonus1Name = new ComboBox<>(physic);
		bonus1Name.valueProperty().bindBidirectional(disciplin.bonus1NameProperty());
		bonus1Name.setMaxWidth(Double.MAX_VALUE);
		physicDisciplins.getChildren().add(bonus1Name);
		GridPane.setRowIndex(bonus1Name, 2 * i + 1);
		GridPane.setColumnIndex(bonus1Name, 1);

		ObservableList<String> bonus = FXCollections.observableArrayList("----", "+1", "+2", "+3", "+4", "x2", "x3");

		ComboBox<String> bonus1Value = new ComboBox<>(bonus);
		bonus1Value.valueProperty().bindBidirectional(disciplin.bonus1ValueProperty());
		physicDisciplins.getChildren().add(bonus1Value);
		GridPane.setRowIndex(bonus1Value, 2 * i + 1);
		GridPane.setColumnIndex(bonus1Value, 2);

		ComboBox<String> bonus2Name = new ComboBox<>(physic);
		bonus2Name.valueProperty().bindBidirectional(disciplin.bonus2NameProperty());
		bonus2Name.setMaxWidth(Double.MAX_VALUE);
		physicDisciplins.getChildren().add(bonus2Name);
		GridPane.setRowIndex(bonus2Name, 2 * (i + 1));
		GridPane.setColumnIndex(bonus2Name, 1);

		ComboBox<String> bonus2Value = new ComboBox<>(bonus);
		bonus2Value.valueProperty().bindBidirectional(disciplin.bonus2ValueProperty());
		physicDisciplins.getChildren().add(bonus2Value);
		GridPane.setRowIndex(bonus2Value, 2 * (i + 1));
		GridPane.setColumnIndex(bonus2Value, 2);
	}

	public void addMentalDisciplin(Disciplin disciplin)
	{
		int i = 0;
		for (ObjectProperty<Disciplin> p : c.getPhysicDisciplins())
		{
			if (p.get() == disciplin)
				break;
			++i;
		}

		ObservableList<String> mental = FXCollections.observableArrayList("---------------", "Charisme", "Perception", "Pouvoir Physique", "Pouvoir Psychique", "Pouvoir " +
				"Élémentaire");

		TextField mentalDName = new TextField();
		mentalDName.textProperty().bindBidirectional(disciplin.nameProperty());
		mentalDisciplins.getChildren().add(mentalDName);
		GridPane.setRowIndex(mentalDName, 2 * i + 1);
		GridPane.setRowSpan(mentalDName, 2);

		ComboBox<String> bonus1Name = new ComboBox<>(mental);
		bonus1Name.valueProperty().bindBidirectional(disciplin.bonus1NameProperty());
		bonus1Name.setMaxWidth(Double.MAX_VALUE);
		mentalDisciplins.getChildren().add(bonus1Name);
		GridPane.setRowIndex(bonus1Name, 2 * i + 1);
		GridPane.setColumnIndex(bonus1Name, 1);

		ObservableList<String> bonus = FXCollections.observableArrayList("----", "+1", "+2", "+3", "+4", "x2", "x3");

		ComboBox<String> bonus1Value = new ComboBox<>(bonus);
		bonus1Value.valueProperty().bindBidirectional(disciplin.bonus1ValueProperty());
		mentalDisciplins.getChildren().add(bonus1Value);
		GridPane.setRowIndex(bonus1Value, 2 * i + 1);
		GridPane.setColumnIndex(bonus1Value, 2);

		ComboBox<String> bonus2Name = new ComboBox<>(mental);
		bonus2Name.valueProperty().bindBidirectional(disciplin.bonus2NameProperty());
		bonus2Name.setMaxWidth(Double.MAX_VALUE);
		mentalDisciplins.getChildren().add(bonus2Name);
		GridPane.setRowIndex(bonus2Name, 2 * (i + 1));
		GridPane.setColumnIndex(bonus2Name, 1);

		ComboBox<String> bonus2Value = new ComboBox<>(bonus);
		bonus2Value.valueProperty().bindBidirectional(disciplin.bonus2ValueProperty());
		mentalDisciplins.getChildren().add(bonus2Value);
		GridPane.setRowIndex(bonus2Value, 2 * (i + 1));
		GridPane.setColumnIndex(bonus2Value, 2);
	}

	public void addSpecialAbility(StringProperty prop)
	{
		TextField field = new TextField();
		field.textProperty().bindBidirectional(prop);
		prop.addListener((observableValue, s, t1) ->
		{
			PacketMixSendModification pkt = PacketManager.createPacket(PacketMixSendModification.class);
			pkt.setCharacter(c);
			pkt.setId("specialAbility" + c.getSpecialAbilities().indexOf(prop));
			pkt.setValue(t1);
			PacketManager.sendPacketToServer(pkt);
		});
		specialAbilities.getChildren().add(specialAbilities.getChildren().size() - 1, field);
	}

	public void addSpecialEffect(StringProperty prop)
	{
		TextField field = new TextField();
		field.textProperty().bindBidirectional(prop);
		prop.addListener((observableValue, s, t1) ->
		{
			PacketMixSendModification pkt = PacketManager.createPacket(PacketMixSendModification.class);
			pkt.setCharacter(c);
			pkt.setId("specialEffect" + c.getSpecialEffects().indexOf(prop));
			pkt.setValue(t1);
			PacketManager.sendPacketToServer(pkt);
		});
		specialEffects.getChildren().add(specialEffects.getChildren().size() - 1, field);
	}

	public void setCharacter(Character c)
	{
		this.c = c;
	}

	public void addPhysicDisciplin()
	{
		int i = c.addPhysicDisciplin();

		c.getPhysicDisciplin(i).setUuid(UUID.randomUUID());
		c.getPhysicDisciplin(i).setPhysic();

		addPhysicDisciplin(c.getPhysicDisciplin(i));

		PacketMixNewDisciplin pkt = PacketManager.createPacket(PacketMixNewDisciplin.class);
		pkt.setCharacter(c);
		pkt.setDisciplin(c.getPhysicDisciplin(i));
		PacketManager.sendPacketToServer(pkt);
	}

	public void addMentalDisciplin()
	{
		int i = c.addMentalDisciplin();

		c.getMentalDisciplin(i).setUuid(UUID.randomUUID());
		c.getMentalDisciplin(i).setMental();

		addMentalDisciplin(c.getMentalDisciplin(i));

		PacketMixNewDisciplin pkt = PacketManager.createPacket(PacketMixNewDisciplin.class);
		pkt.setCharacter(c);
		pkt.setDisciplin(c.getMentalDisciplin(i));
		PacketManager.sendPacketToServer(pkt);
	}

	public void addSpecialAbility()
	{
		int i = c.addSpecialAbility();
		addSpecialAbility(c.getSpecialAbilities().get(i));

		PacketMixNewSpecial pkt = PacketManager.createPacket(PacketMixNewSpecial.class);
		pkt.setCharacter(c);
		pkt.setAbility();
		PacketManager.sendPacketToServer(pkt);
	}

	public void addSpecialEffect()
	{
		int i = c.addSpecialEffect();
		addSpecialEffect(c.getSpecialEffects().get(i));

		PacketMixNewSpecial pkt = PacketManager.createPacket(PacketMixNewSpecial.class);
		pkt.setCharacter(c);
		pkt.setEffect();
		PacketManager.sendPacketToServer(pkt);
	}
}
